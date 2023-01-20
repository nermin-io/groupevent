import React from "react";
import { styled } from "@stitches/react";
import { blackA } from "@radix-ui/colors";
import { CheckIcon } from "@radix-ui/react-icons";
import * as CheckboxPrimitive from "@radix-ui/react-checkbox";

interface Props extends CheckboxPrimitive.CheckboxProps {}

const StyledCheckbox = styled(CheckboxPrimitive.Root, {
  all: "unset",
  backgroundColor: "white",
  width: 20,
  height: 20,
  borderRadius: 3,
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  border: "1px solid #E8E8E8",
  "&:hover": { backgroundColor: blackA.blackA1 },
  "&:focus": { boxShadow: `0 0 0 2px black` },
  '&[data-state="checked"]': {
    backgroundColor: "#682F95",
    border: "1px solid #682F95",
  },
});

const StyledIndicator = styled(CheckboxPrimitive.Indicator, {
  color: blackA.blackA12,
  display: "flex",
});

export const BaseCheckbox = StyledCheckbox;
export const CheckboxIndicator = StyledIndicator;

const Checkbox: React.FC<Props> = ({ checked = false, onCheckedChange }) => (
  <BaseCheckbox checked={checked} onCheckedChange={onCheckedChange}>
    <CheckboxIndicator>
      <CheckIcon color="white" height={20} width={20} />
    </CheckboxIndicator>
  </BaseCheckbox>
);

export default Checkbox;
