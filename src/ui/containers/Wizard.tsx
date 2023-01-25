import React, { useState, useEffect } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Button from "../components/Button";
import useLocalStorage from "../hooks/storage";
import {StorageState} from "../context/storage";
import { isBefore } from 'date-fns';

const INITIAL_STEP = 0;

interface Props {
  components: Array<React.FC>;
}

const isStepValid = (state: StorageState, step: number) => {
  switch(step) {
    case 0:
      return state.name.length > 0 && state.description.length > 0;
    case 1:
      return state.address.length > 0 && state.city.length > 0 && state.state.length > 0 && state.postCode.length > 0;
    case 2:
      return state.date && state.timeFrom && state.timeTo && isBefore(state.timeFrom, state.timeTo);
    case 3:
      return state.attendees.length >= 1;
    default: return false;
  }
}

const Wizard: React.FC<Props> = ({ components}) => {
  const [step, setStep] = useState(INITIAL_STEP);
  const { persist, state } = useLocalStorage();
  const [isValid, setIsValid] = useState(isStepValid(state, INITIAL_STEP));

  const handlePreviousStep = () => {
    if (step === 0) return;
    setStep((currentStep) => currentStep - 1);
    persist();
  };

  const handleNextStep = () => {
    if (step === components.length - 1) return;
    setStep((currentStep) => currentStep + 1);
    persist();
  };

  useEffect(() => {
    setIsValid(isStepValid(state, step));
  }, [state, step]);

  const StepComponent = components[step];

  return (
    <Box>
      <StepComponent />
      <Flex>
        {step !== 0 && (
          <Button variant="outline" onClick={handlePreviousStep}>
            Go Back
          </Button>
        )}
        <Button onClick={handleNextStep} disabled={!isValid}>
          {step === components.length - 1 ? "Send Invites" : "Continue"}
        </Button>
      </Flex>
    </Box>
  );
};

export default Wizard;
