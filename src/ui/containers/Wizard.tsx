import React, { useState } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Button from "../components/Button";
import useLocalStorage from "../hooks/storage";

interface Props {
  components: Array<React.FC>;
}

const Wizard: React.FC<Props> = ({ components}) => {
  const [step, setStep] = useState(0);
  const { persist } = useLocalStorage();

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
        <Button onClick={handleNextStep} >
          {step === components.length - 1 ? "Send Invites" : "Continue"}
        </Button>
      </Flex>
    </Box>
  );
};

export default Wizard;
