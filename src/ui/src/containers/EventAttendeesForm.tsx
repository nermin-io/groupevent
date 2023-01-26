import React, { useEffect } from "react";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Label from "@/components/Label";
import Text from "@/components/Text";
import ListInput, {ListInputOnNewValueHandler, ListInputOnRemoveValueHandler } from "@/components/ListInput";
import useLocalStorage from "@/hooks/storage";

interface Props  {}

const EventAttendeesForm: React.FC<Props> = () => {
  const { state, setField, persist } = useLocalStorage();

  const addValueHandler: ListInputOnNewValueHandler = (values: Array<string>) => {
      setField('attendees', [...state.attendees, ...values]);
  }

  const removeValueHandler: ListInputOnRemoveValueHandler = (value: string) => {
      setField('attendees', state.attendees.filter(attendee => attendee !== value));
  }

  useEffect(() => {
    persist();
  }, [state.attendees]);

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        Who
      </Text>
      <Flex css={{ flexDirection: "column", marginBottom: 8 }}>
        <Box>
          <Label htmlFor="attendees">Invite List</Label>
            <ListInput values={state.attendees} onNewValue={addValueHandler} onRemoveValue={removeValueHandler} />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventAttendeesForm;
