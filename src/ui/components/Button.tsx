import { styled } from '@stitches/react';
import React from 'react';
import Box from './Box';

type Variant = "primary";
type Size = 'small' | 'regular';

interface Props {
    children: React.ReactNode | React.ReactNode[] | string,
    loading?: boolean;
    loadingText?: string;
    variant?: Variant,
    size?: Size;
    [key: string]: any;
}

const BaseButton = styled('button', {
    all: 'unset',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 4,
    boxSizing: 'border-box',
    fontSize: 14,
    lineHeight: 1,
    fontWeight: 400,
    width: '100%',
    cursor: 'pointer',
    variants: {
      variant: {
        primary: {
          backgroundColor: '#692C98',
          color: '#E3CEF4',
          '&:hover': { backgroundColor: '#682F95' },
          '&:focus': { boxShadow: `0 0 0 2px black` },
          '&.disabled': {
            backgroundColor: '#C9A4E5',
            cursor: 'not-allowed'
          }
        }
      },
      size: {
        small: {
          height: 35
        },
        regular: {
          height: 45
        }
      }
    },
    defaultVariants: {
      variant: 'primary',
      size: 'regular'
    }
  });

  const Button: React.FC<Props> = ({children, variant='primary', size='regular', loadingText='Loading...', loading = false,  iconBefore, iconAfter, ...props }) => {

    return (
      <Box>
        { loading ?
            <BaseButton variant={variant} size={size} disabled className='disabled' {...props}>
                { loadingText }
            </BaseButton>
            :
            <BaseButton variant={variant} size={size} className={props.disabled ? 'disabled' : ''} {...props}>
                { children }
            </BaseButton>
        }
      </Box>
    );
  };

  export default Button;