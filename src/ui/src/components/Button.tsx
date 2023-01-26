import { styled } from "@/stitches.config";
import React from "react";
import Box from "@/components/Box";

const variants = {
  primary: {
    backgroundColor: "#692C98",
    color: "#F3E4FF",
    "&:hover": { backgroundColor: "#682F95" },
    "&:focus": { boxShadow: `0 0 0 2px black` },
    "&.disabled": {
      backgroundColor: "#C9A4E5",
      cursor: "not-allowed",
    },
  },
  outline: {
    backgroundColor: "#FFF",
    color: "#343434",
    border: "1px solid #E2E2E2",
    "&:hover": { backgroundColor: "#F7F7F7" },
    "&:focus": { boxShadow: `0 0 0 2px black` },
    "&.disabled": {
      backgroundColor: "#F7F7F7",
      cursor: "not-allowed",
      color: "#B8B8B8",
    },
  },
};

const sizes = {
  small: {
    height: 35,
  },
  regular: {
    height: 45,
  },
};

interface Props {
  children: React.ReactNode | React.ReactNode[] | string;
  loading?: boolean;
  loadingText?: string;
  variant?: keyof typeof variants;
  size?: keyof typeof sizes;

  [key: string]: any;
}

const BaseButton = styled("button", {
  all: "unset",
  display: "inline-flex",
  alignItems: "center",
  justifyContent: "center",
  borderRadius: 4,
  boxSizing: "border-box",
  fontSize: 14,
  lineHeight: 1,
  fontWeight: 400,
  width: "100%",
  cursor: "pointer",
  variants: {
    variant: variants,
    size: sizes,
  },
  defaultVariants: {
    variant: "primary",
    size: "regular",
  },
});

const Button: React.FC<Props> = ({
  children,
  variant = "primary",
  size = "regular",
  loadingText = "Loading...",
  loading = false,
  iconBefore,
  iconAfter,
  ...props
}) => {
  return (
    <Box css={{ width: "100%" }}>
      {loading ? (
        <BaseButton
          variant={variant}
          size={size}
          disabled
          className="disabled"
          {...props}
        >
          {loadingText}
        </BaseButton>
      ) : (
        <BaseButton
          variant={variant}
          size={size}
          className={props.disabled ? "disabled" : ""}
          {...props}
        >
          {children}
        </BaseButton>
      )}
    </Box>
  );
};

export default Button;
