import React from "react";
import { GetServerSideProps, NextPage } from "next";
import Card from "@/components/Card";
import Text from "@/components/Text";
import Groupevent from "@/clients/groupevent";
import Flex from "@/components/Flex";
import {Invite, InviteResponse} from "@/clients/groupevent/types";
import EventResponseForm from "@/containers/EventResponseForm";

interface PageProps {
  error?: {
    message: string;
  };
  invite: Invite,
  answer?: string
}

interface ErrorProps {
  message: string;
}

const ErrorMessage: React.FC<ErrorProps> = ({ message }) => {
  return (
    <Card>
      <Flex css={{ gap: 20, flexDirection: "column" }}>
        <Text css={{ fontSize: 28, fontWeight: 500 }}>Error</Text>
        <Text>{message}</Text>
      </Flex>
    </Card>
  );
};

const EventResponse: NextPage<PageProps> = ({ error, invite, answer }) => {
  if (error) return <ErrorMessage message={error.message} />;

  return (
    <>
      <Card>
        <EventResponseForm invite={invite} answer={answer} />
      </Card>
    </>
  );
};

export const getServerSideProps: GetServerSideProps = async ({ query }) => {
  const { attendee, event, answer } = query;

  if (!attendee || !event)
    return {
      props: {
        error: {
          message: "No event or attendee provided.",
        },
      },
    };

  try {
    const response = await Groupevent.get(
      `attendees/${attendee}/invites/${event}`
    );

    console.log(response.data);

    if(response.status === 200) return {
        props: {
          invite: response.data as Invite,
          answer: answer || null
        }
      }

    return {
      props: {
        error: {
          message: response.data.message
        }
      }
    }

  } catch (err) {
    return {
      props: {
        error: {
          message: "Something went wrong. Please try again later.",
        },
      },
    };
  }
};

export default EventResponse;
