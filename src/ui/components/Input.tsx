import React from 'react';
import Text from './Text';
import { CSS, styled } from '@stitches/react';

interface Props {
  prefix?: string;
  suffix?: string;
  size?: keyof typeof sizeVariants;
  css?: CSS;
  onChange?: React.ChangeEventHandler<HTMLInputElement>;
  [key: string]: any;
}

const sizeVariants = {
  large: {
    height: 45,
    padding: '0 16px',
  },
  small: {
    height: 40,
    padding: '0 12px',
  }
}

const InputWrapper = styled('div', {
  borderRadius: 4,
  border: '1px solid #E2E2E2',
  display: 'flex',
  alignItems: 'center',
  backgroundColor: 'white',
  justifyContent: 'center',
  margin: 0,
  height: 45,
  width: '100%',
  '&:focus-within': { boxShadow: `0 0 0 2px black` },
  '&:has(input:disabled)': {
    backgroundColor: '#F7F7F7',
    cursor: 'not-allowed'
  },
  marginBottom: 8,
  variants: {
    size: sizeVariants
  },
  defaultVariants: {
    size: 'large'
  }
});

const BaseInput = styled('input', {
    all: 'unset',
    boxSizing: 'border-box',
    fontSize: 14,
    lineHeight: 1,
    display: 'block',
    width: '100%',
    height: '100%',
  });

  const Input: React.FC<Props> = ({prefix='', css={}, size='large', suffix='', ...props}) => {
    return (
      <InputWrapper size={size} css={css}>
        { prefix && <Text css={{marginRight: 5, color: '#7D7D7D'}}>{prefix}</Text>}
        <BaseInput {...props} />
        { suffix && <Text css={{color: '#7D7D7D', marginLeft: 5}}>{suffix}</Text>}
      </InputWrapper>
    )
  }

  export default Input;