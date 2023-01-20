import React from "react";
import { styled } from "../stitches.config";
import Box from "./Box";

const Card = styled(Box, {
  background: "white",
  borderRadius: 6,
  padding: 30,
  boxShadow: "0 2px 12px rgba(0,0,0,.1)",
});

export default Card;
