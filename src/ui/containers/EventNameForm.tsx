import React, { useState, useEffect } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Label from "../components/Label";
import Input from "../components/Input";
import Textarea from "../components/Textarea";
import Text from "../components/Text";
import {WizardComponentProps} from "./Wizard";

interface Props extends WizardComponentProps {}

const EventNameForm: React.FC<Props> = ({ setIsValid }) => {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  useEffect(() => {
    if(name.length === 0 || description.length === 0) {
      setIsValid(false);
    } else {
      setIsValid(true);
    }
  }, [name, description]);

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        Create Event
      </Text>
      <Flex css={{ flexDirection: "column" }}>
        <Box>
          <Label htmlFor="eventName">Event Name</Label>
          <Input placeholder="26th Birthday" id="eventName" value={name} onChange={e => setName(e.target.value)} />
        </Box>
        <Box>
          <Label htmlFor="description">Description</Label>
          <Textarea
            placeholder="I'm throwing a birthday party..."
            id="description"
            value={description}
            onChange={e => setDescription(e.target.value)}
          />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventNameForm;
