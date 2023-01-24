import React, { useState } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Button from "../components/Button";

interface Props {
  components: Array<React.FC<WizardComponentProps>>;
}

export type OnValidityChangeHandler = (isValid: boolean) => void;

export interface WizardComponentProps {
  setIsValid: OnValidityChangeHandler;
}

const Wizard: React.FC<Props> = ({ components }) => {
  const [step, setStep] = useState(0);
  const [isValid, setIsValid] = useState(false);

  const handlePreviousStep = () => {
      if(step === 0) return;
      setStep(currentStep => currentStep - 1);
  }

  const handleNextStep = () => {
      if(step === components.length - 1) return;
      setStep(currentStep => currentStep + 1);
      setIsValid(false);
  }

  const validityChangeHandler: OnValidityChangeHandler = (isValid: boolean) => {
    setIsValid(isValid);
  }

  const StepComponent = components[step];
  return (
    <Box>
      <StepComponent setIsValid={validityChangeHandler}/>
      <Flex>
          { step !== 0 && (
              <Button variant="outline" onClick={handlePreviousStep}>Go Back</Button>
          )}
        <Button onClick={handleNextStep} disabled={!isValid}>{step === components.length - 1 ? 'Send Invites' : 'Continue'}</Button>
      </Flex>
    </Box>
  );
};

export default Wizard;
