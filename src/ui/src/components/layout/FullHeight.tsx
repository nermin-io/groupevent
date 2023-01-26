import { styled } from "@/stitches.config";
import Box from "../Box";

const FullHeight = styled(Box, {
  minHeight: "100vh",
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
});

export default FullHeight;
