import React, { useState, useEffect } from "react";
import Flex from "../components/Flex";
import Checkbox from "../components/Checkbox";
import Button from "../components/Button";
import Input from "../components/Input";
import Text from "../components/Text";
import Box from "../components/Box";

interface Props {}

const GettingStarted: React.FC<Props> = () => {
  const [isChecked, setIsChecked] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");

  const [isValid, setIsValid] = useState(false);

  useEffect(() => {
    if (
      isChecked &&
      firstName.length > 0 &&
      lastName.length > 0 &&
      email.length > 0
    ) {
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }, [isChecked, firstName, lastName, email]);

  return (
    <>
      <Text css={{ lineHeight: 1.375, fontWeight: 450, marginBottom: 25 }}>
        Easily invite friends to your event. <br />
        No signup, no marketing, no bs.
      </Text>
      <Flex>
        <Input
          placeholder="First Name"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <Input
          placeholder="Last Name"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
      </Flex>
      <Input
        placeholder="Email Address"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <Flex
        css={{
          justifyContent: "flex-start",
          alignItems: "center",
          marginTop: 35,
        }}
      >
        <Checkbox
          checked={isChecked}
          onCheckedChange={(checked) => setIsChecked(checked as boolean)}
        />
        <Text css={{ color: "#777", fontSize: 14 }}>
          I agree with the terms and conditions.
        </Text>
      </Flex>
      <Box css={{ marginTop: 75 }}>
        <Button disabled={!isValid}>Get Started</Button>
      </Box>
    </>
  );
};

export default GettingStarted;
