import React from "react";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Label from "@/components/Label";
import Input from "@/components/Input";
import Textarea from "@/components/Textarea";
import Text from "@/components/Text";
import useLocalStorage from "@/hooks/storage";

interface Props {}

const EventNameForm: React.FC<Props> = () => {
  const { state, setField } = useLocalStorage();

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        Create Event
      </Text>
      <Flex css={{ flexDirection: "column" }}>
        <Box>
          <Label htmlFor="eventName">Event Name</Label>
          <Input
            placeholder="26th Birthday"
            autocomplete="off"
            autocorrect="off"
            autocapitalize="off"
            spellcheck="false"
            id="eventName"
            value={state.name}
            onChange={(e) => setField("name", e.target.value)}
          />
        </Box>
        <Box>
          <Label htmlFor="description">Description</Label>
          <Textarea
            placeholder="I'm throwing a birthday party..."
            id="description"
            autocomplete="off"
            autocorrect="off"
            autocapitalize="off"
            spellcheck="false"
            value={state.description}
            onChange={(e) => setField("description", e.target.value)}
          />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventNameForm;
