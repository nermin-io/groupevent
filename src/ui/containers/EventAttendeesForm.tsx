import React, { useState, useEffect } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Label from "../components/Label";
import Text from "../components/Text";
import ListInput, {ListInputOnNewValueHandler, ListInputOnRemoveValueHandler } from "../components/ListInput";
import {WizardComponentProps} from "./Wizard";

export const MIN_ATTENDEES = 1;
export const MAX_ATTENDEES = 250;

interface Props extends WizardComponentProps {}

const EventAttendeesForm: React.FC<Props> = ({ setIsValid }) => {

  const [attendees, setAttendees] = useState<Array<string>>([]);

  useEffect(() => {
    if(attendees.length >= MIN_ATTENDEES && attendees.length <= MAX_ATTENDEES) {
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }, [attendees]);

  const addValueHandler: ListInputOnNewValueHandler = (values: Array<string>) => {
      setAttendees(currentAttendees => [...currentAttendees, ...values]);
  }

  const removeValueHandler: ListInputOnRemoveValueHandler = (value: string) => {
      setAttendees(currentAttendees => currentAttendees.filter(attendee => attendee !== value));
  }

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        Who
      </Text>
      <Flex css={{ flexDirection: "column", marginBottom: 8 }}>
        <Box>
          <Label htmlFor="attendees">Invite List</Label>
            <ListInput values={attendees} onNewValue={addValueHandler} onRemoveValue={removeValueHandler} />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventAttendeesForm;
