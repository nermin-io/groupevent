import React from "react";
import { Root, Item } from "@radix-ui/react-toggle-group";
import { styled } from "@/stitches.config";
import { violet, blackA, mauve } from "@radix-ui/colors";
import { startCase } from 'lodash';

export type ToggleGroupOnValueChangeHandler = (val: string) => void;
export type ToggleGroupOption = {
  value: string;
  label: string;
};

interface Props {
  options: Array<ToggleGroupOption>;
  value: string;
  onChange: ToggleGroupOnValueChangeHandler;
  label?: string;
}

const ToggleGroupRoot = styled(Root, {
  width: '100%',
  display: "inline-flex",
  justifyContent: 'center',
  backgroundColor: mauve.mauve6,
  borderRadius: 4,
  boxShadow: `0 2px 10px ${blackA.blackA7}`,
});

const ToggleGroupItem = styled(Item, {
  all: "unset",
  backgroundColor: "white",
  color: mauve.mauve11,
  height: 35,
  display: "flex",
  width: '100%',
  fontSize: 15,
  lineHeight: 1,
  alignItems: "center",
  justifyContent: "center",
  marginLeft: 1,
  "&:first-child": {
    marginLeft: 0,
    borderTopLeftRadius: 4,
    borderBottomLeftRadius: 4,
  },
  "&:last-child": { borderTopRightRadius: 4, borderBottomRightRadius: 4 },
  "&:hover": { backgroundColor: violet.violet3 },
  "&[data-state=on]": {
    backgroundColor: violet.violet5,
    color: violet.violet11,
  },
  "&:focus": { position: "relative", boxShadow: `0 0 0 2px black` },
});

const ToggleGroup: React.FC<Props> = ({ options, value, onChange, label }) => {
  return (
    <ToggleGroupRoot
      type="single"
      aria-label={label}
      value={value}
      onValueChange={onChange}
    >
      { options.map((option, optionIdx) => (
        <ToggleGroupItem key={`${option.value}-${optionIdx}`} value={option.value} aria-label={startCase(option.label)}>
          { option.label }
        </ToggleGroupItem>
      ))}
    </ToggleGroupRoot>
  );
};


export default ToggleGroup;
