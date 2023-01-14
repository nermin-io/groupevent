import React, { useState } from 'react';
import Flex from "../components/Flex";
import Checkbox from "../components/Checkbox";
import Button from "../components/Button";
import Input from "../components/Input";
import Text from '../components/Text';
import Box from "../components/Box";

interface Props {}

const GettingStarted: React.FC<Props> = () => {

    const [isChecked, setIsChecked] = useState(false);

    return (
        <>
            <Text css={{lineHeight: 1.375, fontWeight: 450, marginBottom: 25}}>
                Easily invite friends to your event. <br/>
                No signup, no marketing, no bs.
            </Text>
            <Flex>
                <Input placeholder="First Name"/>
                <Input placeholder="Last Name"/>
            </Flex>
            <Input placeholder="Email Address"/>
            <Flex css={{justifyContent: 'flex-start', alignItems: 'center', marginTop: 35}}>
                <Checkbox checked={isChecked} onCheckedChange={checked => setIsChecked(checked as boolean)}/>
                <Text css={{color: '#777', fontSize: 14}}>I agree with the terms and conditions.</Text>
            </Flex>
            <Box css={{marginTop: 75}}>
                <Button disabled={!isChecked}>Submit</Button>
            </Box>

        </>
    );
};

export default GettingStarted;