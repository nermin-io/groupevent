import React, {useEffect, useState} from "react";
import {EventResponse, Invite, InviteResponse} from "@/clients/groupevent/types";
import Text from "@/components/Text";
import ToggleGroup from "@/components/ToggleGroup";
import Label from "@/components/Label";
import Textarea from "@/components/Textarea";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Input from "@/components/Input";
import Spacer from "@/components/layout/Spacer";
import Button from "@/components/Button";
import {useMutation} from 'react-query';
import Proxy from "@/clients/proxy";
import Notification from "@/components/Notification";
import { styled } from '@/stitches.config';
import { isBefore } from 'date-fns';

interface ResponseProps {
  invite: Invite;
  response: InviteResponse;
}

const FormContainer = styled(Box, {
  display: 'flex',
  flexDirection: 'column',
  gap: 10
});

const ResponseMessage: React.FC<ResponseProps> = ({invite, response}) => {
  return (
    <>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        { response === InviteResponse.GOING ? "Get excited!" : "Maybe next time!" }
      </Text>
      <Flex css={{flexDirection: 'column'}}>
        <Text>
          { response === InviteResponse.GOING ?
          `${invite.event.organiser?.first_name} is looking forward to seeing you there!` :
          "You may not be able to make this one, but there's many more to come in the future!"
         }
        </Text>
      </Flex>
    </>
  )
}

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

const parseResponse = (response: string) => {
  return response === "GOING" ? InviteResponse.GOING : InviteResponse.NOT_GOING;
}

const isCancelled = (invite: Invite) => {
  return invite.event.status === "CANCELLED";
}

const hasLapsed = (invite: Invite) => {
  const scheduledDate = new Date(invite.event.scheduled_date);
  const currentDate = new Date();

  return isBefore(scheduledDate, currentDate);
}

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

  const [responded, setResponded] = useState(false);

  useEffect(() => {
    if(response && message.length > 0 && firstName.length > 0 && lastName.length > 0) {
      setValid(true);
    } else {
      setValid(false);
    }
  }, [response, message, firstName, lastName]);

  const mutation = useMutation(
    (eventResponse: EventResponse) => {
      return Proxy.post("/invites", {
        attendee: invite.attendee.id,
        event: invite.event.id,
        data: eventResponse,
      });
    },
    {
      onSuccess: async (res) => {
        setResponded(true);
      },
    }
  );

  const handleSendEventResponse = () => {
    const eventResponse: EventResponse = {
      first_name: firstName,
      last_name: lastName,
      response: parseResponse(response),
      message: message
    };

    mutation.mutate(eventResponse);
  }

  if(responded)
    return <ResponseMessage invite={invite} response={parseResponse(response)} />

  return (
    <FormContainer>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        {`${invite.event.organiser?.first_name}'s ${invite.event.name}`}
      </Text>
      { isCancelled(invite) && (
        <Notification type='warning' title='This event has been cancelled' description='You are no longer able to respond to this invite.' />
      )}
      { hasLapsed(invite) && (
        <Notification type='warning' title='This event is in the past' description='You are no longer able to respond to this invite.' />
      )}
      <Flex css={{gap: 12, flexDirection: 'column'}}>
        <ToggleGroup
          options={responseOptions}
          value={response}
          onChange={(val) => setResponse(val)}
        />
        <Spacer css={{height: 12}} />
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
        <Button disabled={!valid || isCancelled(invite) || hasLapsed(invite) } onClick={handleSendEventResponse} loading={mutation.isLoading} loadingText="Sending response...">Send</Button>
      </Flex>
    </FormContainer>
  );
};

export default EventResponseForm;
