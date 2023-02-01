import React, { useEffect, useState } from "react";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Label from "@/components/Label";
import Textarea from "@/components/Textarea";
import Text from "@/components/Text";
import Button from "@/components/Button";
import { Event, EventStatus } from "@/clients/groupevent/types";
import { useMutation } from "react-query";
import Proxy from "@/clients/proxy";
import Notification from "@/components/Notification";

interface Props {
  event: Event;
  token: string;
}

const EventCancelForm: React.FC<Props> = ({ event , token}) => {
  const [message, setMessage] = useState("");

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [valid, setValid] = useState(false);

  useEffect(() => {
    if(message.length > 0) {
      setValid(true);
    } else {
      setValid(false);
    }
  }, [message]);

  const mutation = useMutation(
    (data: { message: string; }) => {
      return Proxy.post("/cancel-event", {
        organiser: event.organiser?.id,
        token: token,
        event: event.id,
        data: data,
      });
    },
    {
      onSuccess: async (res) => {
        if(res.status >= 200 && res.status < 300) {
          setSuccess("The event has successfully been cancelled.");
        } else {
          setError(res.data.message);
        }
      },
    }
  );

  const handleCancellation = () => {
    mutation.mutate({
      message: message
    });
  }

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        {event.name}
      </Text>
      { event.status === EventStatus.CANCELLED && (
        <Box css={{marginBottom: 20}}>
          <Notification type='warning' title='This event is cancelled' description='Submission has been disabled.' />
        </Box>
      )}
      { error && (
        <Box css={{marginBottom: 20}}>
          <Notification type='error' title='Error' description={error} />
        </Box>
      )}
      { success && (
        <Box css={{marginBottom: 20}}>
          <Notification type='success' title='Successful' description={success} />
        </Box>
      )}
      <Flex css={{ flexDirection: "column", gap: 8, marginBottom: 8 }}>
        <Box>
          <Label htmlFor="message">Message</Label>
          <Textarea
            placeholder="Please provide a reason for cancellation."
            id="message"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />
        </Box>
        <Button
          loading={mutation.isLoading}
          loadingText="Cancelling..."
          onClick={handleCancellation}
          disabled={!valid || event.status === EventStatus.CANCELLED || success}
        >
          Cancel Event
        </Button>
      </Flex>
    </Box>
  );
};

export default EventCancelForm;
