import React from "react";
import { NextPage } from "next";
import Card from "../../components/Card";
import Text from "../../components/Text";
import Flex from "../../components/Flex";
import Input from "../../components/Input";
import Label from "../../components/Label";
import Box from "../../components/Box";
import Textarea from "../../components/Textarea";
import Button from "../../components/Button";

const CreateEvent: NextPage = () => {
  return (
    <>
      <Card>
        <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
          Create Event
        </Text>
        <Flex css={{ flexDirection: "column" }}>
          <Box>
            <Label htmlFor="eventName">Event Name</Label>
            <Input placeholder="26th Birthday" id="eventName" />
          </Box>
          <Box>
            <Label htmlFor="description">Description</Label>
            <Textarea placeholder="I'm throwing a birthday party..." />
          </Box>
          <Flex>
            <Button variant="outline">Cancel</Button>
            <Button>Continue</Button>
          </Flex>
        </Flex>
      </Card>
    </>
  );
};

export default CreateEvent;
