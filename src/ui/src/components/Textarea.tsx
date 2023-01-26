import React from "react";
import { styled } from "@/stitches.config";
import { CSS } from '@stitches/react';

interface Props {
  css?: CSS;
  onChange?: React.ChangeEventHandler<HTMLTextAreaElement>;

  [key: string]: any;
}

const Wrapper = styled("div", {
  borderRadius: 4,
  border: "1px solid #E2E2E2",
  display: "flex",
  alignItems: "center",
  backgroundColor: "white",
  justifyContent: "center",
  margin: 0,
  width: "100%",
  height: "100%",
  "&:focus-within": { boxShadow: `0 0 0 2px black` },
  "&:has(input:disabled)": {
    backgroundColor: "#F7F7F7",
    cursor: "not-allowed",
  },
  marginBottom: 8,
  padding: 16,
});

const TextareaPrimitive = styled("textarea", {
  all: "unset",
  boxSizing: "border-box",
  fontSize: 14,
  lineHeight: 1,
  display: "block",
  width: "100%",
  fontFamily: "inherit",
  resize: "vertical",
  height: 150,
});

const Textarea: React.FC<Props> = ({ css = {}, ...props }) => {
  return (
    <Wrapper css={css}>
      <TextareaPrimitive {...props} />
    </Wrapper>
  );
};

export default Textarea;
