import React, { useState, useEffect } from "react";
import Flex from "@/components/Flex";
import Checkbox from "@/components/Checkbox";
import Button from "@/components/Button";
import Input from "@/components/Input";
import Text from "@/components/Text";
import Box from "@/components/Box";
import { useMutation } from "react-query";
import { Organiser } from "@/clients/groupevent/types";
import Proxy from "@/clients/proxy";
import { useRouter } from "next/router";

interface Props {}

const GettingStarted: React.FC<Props> = () => {
  const [isChecked, setIsChecked] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const router = useRouter();

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

  const sendLinkMutation = useMutation(
    (organiser: Organiser) => {
      return Proxy.post("/links", organiser);
    },
    {
      onSuccess: async (res) => {
        await router.push("/check-email");
      },
    }
  );

  const handleSubmit = () => {
    if (!isValid) return;

    sendLinkMutation.mutate({
      first_name: firstName,
      last_name: lastName,
      email_address: email,
    });
  };

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
          autocomplete="off"
          onChange={(e) => setFirstName(e.target.value)}
        />
        <Input
          placeholder="Last Name"
          value={lastName}
          autocomplete="off"
          onChange={(e) => setLastName(e.target.value)}
        />
      </Flex>
      <Input
        placeholder="Email Address"
        value={email}
        autocomplete="off"
        autocorrect="off"
        autocapitalize="off"
        spellcheck="false"
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
        <Button
          disabled={!isValid}
          loading={sendLinkMutation.isLoading}
          loadingText="Loading..."
          onClick={handleSubmit}
        >
          Get Started
        </Button>
      </Box>
    </>
  );
};

export default GettingStarted;
