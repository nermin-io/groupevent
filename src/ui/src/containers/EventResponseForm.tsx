import React, { useState, useEffect } from "react";
import { Invite, InviteResponse } from "@/clients/groupevent/types";
import Text from "@/components/Text";
import ToggleGroup from "@/components/ToggleGroup";
import Label from "@/components/Label";
import Textarea from "@/components/Textarea";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Input from "@/components/Input";
import Spacer from "@/components/layout/Spacer";
import Button from "@/components/Button";

interface Props {
  invite: Invite;
  answer?: string;
}

const getOptionLabel = (response: InviteResponse) => {
  switch (response) {
    case InviteResponse.GOING:
      return "I'm going";
    case InviteResponse.NOT_GOING:
      return "I'm not going";
  }
};

const responseOptions = Object.values(InviteResponse).map((r) => ({
  value: r,
  label: getOptionLabel(r),
}));

const EventResponseForm: React.FC<Props> = ({ invite, answer }) => {
  const [response, setResponse] = useState(answer || invite.response);
  const [message, setMessage] = useState(invite.message || "");
  const [firstName, setFirstName] = useState(invite.attendee.first_name || '');
  const [lastName, setLastName] = useState(invite.attendee.last_name || '');
  const [valid, setValid] = useState(false);

  useEffect(() => {
    if(response && message.length > 0 && firstName.length > 0 && lastName.length > 0) {
      setValid(true);
    } else {
      setValid(false);
    }
  }, [response, message, firstName, lastName]);

  return (
    <>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        {`${invite.event.organiser?.first_name}'s ${invite.event.name}`}
      </Text>
      <Flex css={{gap: 12, flexDirection: 'column'}}>
        <ToggleGroup
          options={responseOptions}
          value={response}
          onChange={(val) => setResponse(val)}
        />
        <Spacer css={{height: 20}} />
        <Flex>
          <Box css={{width: '100%'}}>
            <Label htmlFor="firstName">First Name</Label>
            <Input id="firstName" placeholder="John" value={firstName} onChange={e => setFirstName(e.target.value)}/>
          </Box>
          <Box css={{width: '100%'}}>
            <Label htmlFor="lastName">Last Name</Label>
            <Input id="lastName" placeholder="Smith" value={lastName} onChange={e => setLastName(e.target.value)}/>
          </Box>
        </Flex>
        <Box>
          <Label htmlFor="message">Leave a Note</Label>
          <Textarea
            placeholder="See you there!"
            id="message"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />
        </Box>
        <Button disabled={!valid}>Send</Button>
      </Flex>
    </>
  );
};

export default EventResponseForm;
