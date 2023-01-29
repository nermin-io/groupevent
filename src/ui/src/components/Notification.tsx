import React from 'react';
import { styled } from '@/stitches.config';
import Box from "@/components/Box";
import { ExclamationTriangleIcon, InfoCircledIcon, CrossCircledIcon, CheckCircledIcon  } from "@radix-ui/react-icons";
import { red, amber, green, slate, blackA } from '@radix-ui/colors';
import Flex from "@/components/Flex";
import Text from "@/components/Text";

export type NotificationType = 'info' | 'success' | 'warning' | 'error';

interface Props {
  type: NotificationType;
  title: string;
  description: string;
}

const NotificationContainer = styled(Box, {
  display: 'flex',
  flexDirection: 'column',
  border: '1px solid #E2E2E2',
  borderRadius: 4,
  padding: 20,
  gap: 8
});

const NotificationTitle = styled(Flex, {
  flexDirection: 'row',
  justifyContent: 'flex-start',
  alignItems: 'center',
  'p': {
    fontSize: 14,
    fontWeight: 450
  },
  variants: {
    type: {
      info: {
        color: slate.slate10
      },
      error: {
        color: red.red10
      },
      warning: {
        color: amber.amber10
      },
      success: {
        color: green.green10
      }
    }
  }
});

const NotificationDescription = styled(Text, {
  fontSize: 14,
  color: slate.slate12
});

const renderNotificationIcon = (type: NotificationType) => {
  switch(type) {
    case 'info': return InfoCircledIcon;
    case 'error': return CrossCircledIcon;
    case 'warning': return ExclamationTriangleIcon;
    case 'success': return CheckCircledIcon;
  }
}

const Notification: React.FC<Props> = ({ type, title, description}) => {

  const Icon = renderNotificationIcon(type);
  return (
    <NotificationContainer>
        <NotificationTitle type={type}>
          <Icon/>
          <Text>{title}</Text>
        </NotificationTitle>
      <NotificationDescription>{description}</NotificationDescription>
    </NotificationContainer>
  );
};

export default Notification;