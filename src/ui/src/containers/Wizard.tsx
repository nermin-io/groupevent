import React, { useState, useEffect } from "react";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Button from "@/components/Button";
import useLocalStorage from "@/hooks/storage";
import { StorageState } from "@/context/storage";
import { isBefore } from "date-fns";
import { useMutation } from "react-query";
import Proxy from "@/clients/proxy";
import useAuth from "@/hooks/auth";
import { useRouter } from "next/router";
import { Event } from "@/clients/groupevent/types";
import { format } from "date-fns";
import AlertDialog from "@/components/AlertDialog";

const INITIAL_STEP = 0;

interface Props {
  components: Array<React.FC>;
}

const isStepValid = (state: StorageState, step: number) => {
  switch (step) {
    case 0:
      return state.name.length > 0 && state.description.length > 0;
    case 1:
      return (
        state.address.length > 0 &&
        state.city.length > 0 &&
        state.state.length > 0 &&
        state.postCode.length > 0
      );
    case 2:
      return (
        state.date &&
        state.timeFrom &&
        state.timeTo &&
        isBefore(state.timeFrom, state.timeTo) &&
        state.agenda.length > 0
      );
    case 3:
      return state.attendees.length >= 1;
    default:
      return false;
  }
};

const Wizard: React.FC<Props> = ({ components }) => {
  const [step, setStep] = useState(INITIAL_STEP);
  const { persist, state, clear } = useLocalStorage();
  const [isValid, setIsValid] = useState(isStepValid(state, INITIAL_STEP));
  const [isRouting, setIsRouting] = useState(false);
  const [serverError, setServerError] = useState("");
  const router = useRouter();

  const auth = useAuth();

  const handlePreviousStep = () => {
    if (step === 0) return;
    setStep((currentStep) => currentStep - 1);
    persist();
  };

  const handleNextStep = () => {
    if (step === components.length - 1) {
      createEvent();
    } else {
      setStep((currentStep) => currentStep + 1);
      persist();
    }
  };

  const clearErrorHandler = () => {
    setServerError("");
  };

  const createEvent = () => {
    const event = {
      name: state.name,
      description: state.description,
      address: {
        address: state.address,
        city: state.city,
        post_code: state.postCode,
        state: state.state,
        notes: state.notes,
      },
      scheduled_date: format(state.date, "yyyy-MM-dd"),
      time_from: format(state.timeFrom, "HH:mm"),
      time_to: format(state.timeTo, "HH:mm"),
      agenda: state.agenda,
      attendees: state.attendees,
    };

    mutation.mutate(event);
  };

  useEffect(() => {
    setIsValid(isStepValid(state, step));
  }, [state, step]);

  const mutation = useMutation(
    (event: Event) => {
      return Proxy.post("/events", {
        organiser: auth.session?.id,
        data: event,
      });
    },
    {
      onSuccess: async (res) => {
        if (res.status >= 200 && res.status < 300) {
          clear();
          setIsRouting(true);
          await router.push("/event/success");
        } else {
          setServerError(res.data.message);
        }
      },
    }
  );

  const StepComponent = components[step];

  return (
    <Box>
      <AlertDialog
        title="Error"
        description={serverError}
        onClose={clearErrorHandler}
      />
      <StepComponent />
      <Flex>
        {step !== 0 && (
          <Button variant="outline" onClick={handlePreviousStep}>
            Go Back
          </Button>
        )}
        <Button
          onClick={handleNextStep}
          disabled={!isValid}
          loading={mutation.isLoading || isRouting}
          loadingText="Creating event..."
        >
          {step === components.length - 1 ? "Send Invites" : "Continue"}
        </Button>
      </Flex>
    </Box>
  );
};

export default Wizard;
