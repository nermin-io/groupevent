import React from 'react';
import * as LabelPrimitive from '@radix-ui/react-label';
import { styled } from '@stitches/react';

const Label = styled(LabelPrimitive.Root, {
  fontSize: 14,
  fontWeight: 450,
  color: '#000',
  lineHeight: 2
});

export default Label;