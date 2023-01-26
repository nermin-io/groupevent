import React from "react";
import Box from "@/components/Box";
import Flex from "@/components/Flex";
import Label from "@/components/Label";
import Textarea from "@/components/Textarea";
import Text from "@/components/Text";
import DatePicker from "@/components/DatePicker";
import useLocalStorage from "@/hooks/storage";

interface Props {}

const EventTimeForm: React.FC<Props> = () => {
  const { state, setField } = useLocalStorage();

  return (
    <Box>
      <Text css={{ fontSize: 20, fontWeight: 450, marginBottom: 32 }}>
        When
      </Text>
      <Flex css={{ flexDirection: "column", gap: 8, marginBottom: 8 }}>
        <Box>
          <Label htmlFor="date">Date</Label>
          <DatePicker
            selected={state.date}
            onChange={(date: Date) => setField('date', date)}
            id="date"
          />
        </Box>
        <Flex>
          <Box css={{ width: "100%" }}>
            <Label htmlFor="fromTime">From</Label>
            <DatePicker
              selected={state.timeFrom}
              onChange={(date: Date) => { console.log(date); setField('timeFrom', date);}}
              type="time"
              id="fromTime"
            />
          </Box>
          <Box css={{ width: "100%" }}>
            <Label htmlFor="toTime">To</Label>
            <DatePicker
              selected={state.timeTo}
              onChange={(date: Date) => {console.log(date); setField('timeTo', date);}}
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
            value={state.agenda}
            onChange={(e) => setField('agenda', e.target.value)}
          />
        </Box>
      </Flex>
    </Box>
  );
};

export default EventTimeForm;
