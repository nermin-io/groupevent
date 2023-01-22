import React, { useState } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Button from "../components/Button";

interface Props {
  components: Array<React.FC>;
}

const Wizard: React.FC<Props> = ({ components }) => {
  const [step, setStep] = useState(2);

  const handlePreviousStep = () => {
      if(step === 0) return;
      setStep(currentStep => currentStep - 1);
  }

  const handleNextStep = () => {
      if(step === components.length - 1) return;
      setStep(currentStep => currentStep + 1);
  }

  const StepComponent = components[step];
  return (
    <Box>
      <StepComponent />
      <Flex>
        <Button variant="outline" onClick={handlePreviousStep}>{step === 0 ? 'Cancel' : 'Go Back'}</Button>
        <Button onClick={handleNextStep} disabled={step === components.length - 1}>Continue</Button>
      </Flex>
    </Box>
  );
};

export default Wizard;
