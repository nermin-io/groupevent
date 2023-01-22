import React from "react";
import DatePickerPrimitive, { ReactDatePickerProps } from "react-datepicker";
import { styled } from "../stitches.config";
import Box from "./Box";

import "react-datepicker/dist/react-datepicker.css";
import {CalendarIcon, ClockIcon} from "@radix-ui/react-icons";

interface Props extends ReactDatePickerProps {
  type?: "date" | "time";
}

const DatePickerWrapper = styled(Box, {
  input: {
    all: "unset",
    width: "100%",
    height: 45,
    fontSize: 14,
    paddingLeft: 8,
  },
  "&:focus-within": { boxShadow: `0 0 0 2px black` },
  display: "flex",
  alignItems: "center",
  borderRadius: 4,
  border: "1px solid #E2E2E2",
  paddingLeft: 16,
  width: "100%",
});

export const DEFAULT_DATE_FORMAT = "EEEE, d MMMM yyyy";
export const DEFAULT_TIME_FORMAT = "hh:mm a";

const DatePicker: React.FC<Props> = ({ onChange, selected, type = "date", ...props}) => {
  return (
    <DatePickerWrapper>
      {type === "date" ? (
        //  date picker
        <>
          <CalendarIcon color="#B9B9B9" width={20} height={20} />
          <DatePickerPrimitive
            onChange={onChange}
            selected={selected}
            dateFormat={DEFAULT_DATE_FORMAT}
            minDate={new Date()}
            {...props}
          />
        </>
      ) : (
        // time picker
        <>
          <ClockIcon color="#B9B9B9" width={20} height={20} />
          <DatePickerPrimitive
            onChange={onChange}
            selected={selected}
            showTimeSelect
            showTimeSelectOnly
            timeIntervals={10}
            dateFormat={DEFAULT_TIME_FORMAT}
            {...props}
          />
        </>
      )}
    </DatePickerWrapper>
  );
};

export default DatePicker;
