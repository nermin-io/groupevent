import React, { useState } from 'react';
import Flex from "../components/Flex";
import Checkbox from "../components/Checkbox";
import Button from "../components/Button";
import Input from "../components/Input";

interface Props {
}

const GettingStarted: React.FC<Props> = () => {

    const [isChecked, setIsChecked] = useState(false);

    return (
        <>
            <Flex>
                <Input placeholder="First Name"/>
                <Input placeholder="Last Name"/>
            </Flex>
            <Input placeholder="Email Address"/>
            <Checkbox checked={isChecked} onCheckedChange={checked => setIsChecked(checked as boolean)}/>
            <Button disabled={!isChecked}>Submit</Button>
        </>
    )
};

export default GettingStarted;