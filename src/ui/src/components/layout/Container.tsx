import { styled } from "@/stitches.config";
import Box from "../Box";

const Container = styled(Box, {
  width: 500,
  '@small': {
    width: '100%'
  }
});

export default Container;
