import React, { useState } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Button from "../components/Button";

interface Props {
  components: Array<React.FC>;
}

const Wizard: React.FC<Props> = ({ components }) => {
  const [step, setStep] = useState(0);

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
        <Button variant="outline" onClick={handlePreviousStep}>Cancel</Button>
        <Button onClick={handleNextStep}>Continue</Button>
      </Flex>
    </Box>
  );
};

export default Wizard;
