import { styled } from "@stitches/react";
import Box from "./Box";

const Flex = styled(Box, {
  display: "flex",
  justifyContent: "space-between",
  gap: 10,
});

export default Flex;
