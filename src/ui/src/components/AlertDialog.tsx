import React, { useState, useEffect } from 'react';
import { styled, keyframes } from '@/stitches.config';
import * as Alert from '@radix-ui/react-alert-dialog';
import { blackA, mauve } from '@radix-ui/colors';
import Button from "@/components/Button";

export type AlertDialogOnCloseHandler = () => void;

interface Props {
  title: string;
  description: string;
  onClose?: AlertDialogOnCloseHandler;
}

const contentShow = keyframes({
  '0%': { opacity: 0, transform: 'translate(-50%, -48%) scale(.96)' },
  '100%': { opacity: 1, transform: 'translate(-50%, -50%) scale(1)' },
});

const overlayShow = keyframes({
  '0%': { opacity: 0 },
  '100%': { opacity: 1 },
});


const AlertDialogOverlay = styled(Alert.Overlay, {
  backgroundColor: blackA.blackA9,
  position: 'fixed',
  inset: 0,
  animation: `${overlayShow} 150ms cubic-bezier(0.16, 1, 0.3, 1)`,
});

const AlertDialogContent = styled(Alert.Content, {
  backgroundColor: 'white',
  borderRadius: 6,
  boxShadow: 'hsl(206 22% 7% / 35%) 0px 10px 38px -10px, hsl(206 22% 7% / 20%) 0px 10px 20px -15px',
  position: 'fixed',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '90vw',
  maxWidth: '500px',
  maxHeight: '85vh',
  padding: 25,
  animation: `${contentShow} 150ms cubic-bezier(0.16, 1, 0.3, 1)`,

  '&:focus': { outline: 'none' },
});

const AlertDialogTitle = styled(Alert.Title, {
  margin: 0,
  color: mauve.mauve12,
  fontSize: 17,
  fontWeight: 500,
});

const AlertDialogDescription = styled(Alert.Description, {
  marginBottom: 20,
  color: mauve.mauve11,
  fontSize: 15,
  lineHeight: 1.5,
});

const AlertDialog: React.FC<Props> = ({ title, description, onClose = () => {}}) => {
  const [open, setOpen] = useState(description.length > 0);

  useEffect(() => {
    if(description.length > 0) setOpen(true);
    if(description.length === 0) setOpen(false);
  }, [description]);

  useEffect(() => {
    if(!open) onClose();
  }, [open]);

  return (
    <Alert.Root open={open}>
    <Alert.Portal>
      <AlertDialogOverlay />
      <AlertDialogContent>
        <AlertDialogTitle>{ title }</AlertDialogTitle>
        <AlertDialogDescription>
          { description }
        </AlertDialogDescription>
        <Button onClick={() => setOpen(false)}>Ok</Button>
      </AlertDialogContent>
    </Alert.Portal>
  </Alert.Root>
  );
}

export default AlertDialog;


