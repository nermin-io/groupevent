import React, { useState, useEffect } from "react";
import { styled } from "@/stitches.config";
import Box from "@/components/Box";
import Textarea from "@/components/Textarea";
import { Cross2Icon } from "@radix-ui/react-icons";
import { uniq } from "lodash";

export type ListInputOnNewValueHandler = (values: Array<string>) => void;
export type ListInputOnRemoveValueHandler = (value: string) => void;

interface Props {
  values: Array<string>;
  onNewValue: ListInputOnNewValueHandler;
  onRemoveValue: ListInputOnRemoveValueHandler;
}

const ListContainer = styled("ol", {
  all: "unset",
  listStyle: "none",
  display: "flex",
  flexWrap: "wrap",
  gap: 5,
  paddingTop: 12,
  marginBottom: 8,
  width: "100%",
});

const ListItem = styled("li", {
  fontSize: 14,
  color: "#571789",
  backgroundColor: "#F1E9F6",
  borderRadius: 5,
  padding: "4px 4px 4px 10px",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
});

const ListInputWrapper = styled(Box, {
  border: "1px solid #E2E2E2",
  borderRadius: 4,
  backgroundColor: "white",
  margin: 0,
  width: "100%",
  padding: "0 16px 16px 16px",
  "&:focus-within": { boxShadow: `0 0 0 2px black` },
});

const CloseButton = styled("button", {
  all: "unset",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  marginLeft: 7,
  cursor: 'pointer',
  padding: 5,
  borderRadius: 4,
  '&:hover': {
    background: 'rgba(255, 255, 255, .4)'
  }
});

const ListInput: React.FC<Props> = ({ values, onNewValue, onRemoveValue }) => {
  const [text, setText] = useState("");

  const handleTextChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setText(e.target.value);
  };

  const updateValuesWithDelimiter = (delimiter: string) => {
    const filteredValues = text
      .split(delimiter)
      .map((val) => val.trim())
      .filter((val) => val !== "")
      .filter((val) => !values.includes(val));

    const newValues = uniq(filteredValues);

    onNewValue(newValues);
    setText("");
  };

  useEffect(() => {
    if (text.includes(",")) updateValuesWithDelimiter(",");
    else if (text.includes("\n")) updateValuesWithDelimiter("\n");
  }, [text]);

  return (
    <ListInputWrapper>
      <ListContainer>
        {values.map((val, valIndex) => (
          <ListItem key={`${val}-${valIndex}`}>
            {val}
            <CloseButton onClick={() => onRemoveValue(val) } >
              <Cross2Icon color="#571789"/>
            </CloseButton>
          </ListItem>
        ))}
      </ListContainer>
      <Textarea
        autocomplete="off"
        autocorrect="off"
        autocapitalize="off"
        spellcheck="false"
        value={text}
        onChange={handleTextChange}
        css={{ border: 0, padding: 0, "&:focus-within": { boxShadow: `none` } }}
      />
    </ListInputWrapper>
  );
};

export default ListInput;
