import React, { useState, useEffect } from "react";
import Box from "../components/Box";
import Flex from "../components/Flex";
import Label from "../components/Label";
import Textarea from "../components/Textarea";
import Text from "../components/Text";
import DatePicker from "../components/DatePicker";
import { roundToNearestN } from "../helpers";
import {WizardComponentProps} from "./Wizard";
import { isBefore } from 'date-fns';

interface Props extends WizardComponentProps {}

const getCurrentRoundedTime = (): Date => {
  const currentTime = new Date();
  const roundedMinutes = roundToNearestN(currentTime.getMinutes(), 10);

  currentTime.setMinutes(roundedMinutes);
  return currentTime;
};

const getInitialTimeRange = (): Array<Date> => {
  const timeFrom = getCurrentRoundedTime();
  const timeTo = getCurrentRoundedTime();
  timeTo.setMinutes(timeTo.getMinutes() + 15);

  return [timeFrom, timeTo];
};

const EventTimeForm: React.FC<Props> = ({ setIsValid }) => {
  const [initialTimeFrom, initialTimeTo] = getInitialTimeRange();

  const [date, setDate] = useState(new Date());
  const [fromTime, setFromTime] = useState(initialTimeFrom);
  const [toTime, setToTime] = useState(initialTimeTo);
  const [agenda, setAgenda] = useState("");

  useEffect(() => {
    if(date && fromTime && toTime && isBefore(fromTime, toTime)) {
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }, [date, fromTime, toTime]);

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        When
      </Text>
      <Flex css={{ flexDirection: "column", gap: 8, marginBottom: 8 }}>
        <Box>
          <Label htmlFor="date">Date</Label>
          <DatePicker
            selected={date}
            onChange={(date: Date) => setDate(date)}
            id="date"
          />
        </Box>
        <Flex>
          <Box css={{ width: "100%" }}>
            <Label htmlFor="fromTime">From</Label>
            <DatePicker
              selected={fromTime}
              onChange={(date: Date) => setFromTime(date)}
              type="time"
              id="fromTime"
            />
          </Box>
          <Box css={{ width: "100%" }}>
            <Label htmlFor="toTime">To</Label>
            <DatePicker
              selected={toTime}
              onChange={(date: Date) => setToTime(date)}
              type="time"
              id="toTime"
            />
          </Box>
        </Flex>
        <Box>
          <Label htmlFor="agenda">Agenda</Label>
          <Textarea
            placeholder="If your event requires an agenda, provide it here."
            id="agenda"
            value={agenda}
            onChange={(e) => setAgenda(e.target.value)}
          />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventTimeForm;
