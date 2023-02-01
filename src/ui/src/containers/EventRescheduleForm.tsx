import React, { useEffect, useState } from "react";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Label from "@/components/Label";
import Textarea from "@/components/Textarea";
import Text from "@/components/Text";
import DatePicker from "@/components/DatePicker";
import Button from "@/components/Button";
import { Event, EventStatus } from "@/clients/groupevent/types";
import { format, isAfter, isBefore, parse } from "date-fns";
import { useMutation } from "react-query";
import Proxy from "@/clients/proxy";
import Notification from "@/components/Notification";

interface Props {
  event: Event;
  token: string;
}

const parseTime = (timeValue: string) => {
  return new Date(parse(timeValue, "HH:mm:ss", new Date()));
};

const isValidDate = (date: Date) => {
  return date && isAfter(date, new Date());
}

const isValidTimeRange = (time1: Date, time2: Date) => {
  return time1 && time2 && isBefore(time1, time2);
}

const EventRescheduleForm: React.FC<Props> = ({ event , token}) => {
  const [date, setDate] = useState(new Date(event.scheduled_date));
  const [timeFrom, setTimeFrom] = useState(parseTime(event.time_from));
  const [timeTo, setTimeTo] = useState(parseTime(event.time_to));
  const [agenda, setAgenda] = useState(event.agenda);

  const [rescheduled, setRescheduled] = useState(false);
  const [error, setError] = useState("");
  const [valid, setValid] = useState(false);

  useEffect(() => {
    if(isValidDate(date) && isValidTimeRange(timeFrom, timeTo) && agenda) {
      setValid(true);
    } else {
      setValid(false);
    }
  }, [date, timeFrom, timeTo, agenda]);

  const mutation = useMutation(
    (data: Partial<Event>) => {
      return Proxy.post("/reschedule-event", {
        organiser: event.organiser?.id,
        token: token,
        event: event.id,
        data: data,
      });
    },
    {
      onSuccess: async (res) => {
        if(res.status >= 200 && res.status < 300) {
          setRescheduled(true);
        } else {
          setError(res.data.message);
        }

      },
    }
  );

  const handleRescheduleEvent = () => {
    mutation.mutate({
      scheduled_date: format(date, "yyyy-MM-dd"),
      time_from: format(timeFrom, "HH:mm"),
      time_to: format(timeTo, "HH:mm"),
      agenda: agenda,
    });
  };

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
      { rescheduled && (
        <Box css={{marginBottom: 20}}>
          <Notification type='success' title='Successful' description={`${event.name} has successfully been rescheduled.`} />
        </Box>
      )}
      { error && (
        <Box css={{marginBottom: 20}}>
          <Notification type='error' title='Error' description={error} />
        </Box>
      )}
      <Flex css={{ flexDirection: "column", gap: 8, marginBottom: 8 }}>
        <Box>
          <Label htmlFor="date">Date</Label>
          <DatePicker
            selected={date}
            onChange={(scheduledDate: Date) => setDate(scheduledDate)}
            id="date"
          />
        </Box>
        <Flex>
          <Box css={{ width: "100%" }}>
            <Label htmlFor="fromTime">From</Label>
            <DatePicker
              selected={timeFrom}
              onChange={(newTimeFrom: Date) => setTimeFrom(newTimeFrom)}
              type="time"
              id="fromTime"
            />
          </Box>
          <Box css={{ width: "100%" }}>
            <Label htmlFor="toTime">To</Label>
            <DatePicker
              selected={timeTo}
              onChange={(newTimeTo: Date) => setTimeTo(newTimeTo)}
              type="time"
              id="toTime"
            />
          </Box>
        </Flex>
        <Box>
          <Label htmlFor="agenda">Agenda</Label>
          <Textarea
            placeholder="Provide a rough agenda for your event."
            id="agenda"
            value={agenda}
            onChange={(e) => setAgenda(e.target.value)}
          />
        </Box>
        <Button
          loading={mutation.isLoading}
          loadingText="Rescheduling.."
          onClick={handleRescheduleEvent}
          disabled={!valid || event.status === EventStatus.CANCELLED}
        >
          Reschedule Event
        </Button>
      </Flex>
    </Box>
  );
};

export default EventRescheduleForm;
