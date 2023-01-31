import { GetServerSideProps, NextPage } from "next";
import React from "react";
import Card from "@/components/Card";
import Flex from "@/components/Flex";
import Text from "@/components/Text";
import Groupevent from "@/clients/groupevent";
import { Event } from "@/clients/groupevent/types";
import EventRescheduleForm from "@/containers/EventRescheduleForm";

interface PageProps {
  event: Event;
  token: string;
  error?: {
    message: string;
  }
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

const RescheduleEvent: NextPage<PageProps> = ({ event, error, token }) => {
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <Card>
        <EventRescheduleForm event={event} token={token} />
      </Card>
    </>
  );
};

export default RescheduleEvent;

export const getServerSideProps: GetServerSideProps = async ({ query }) => {
  const { token } = query;
  const decodedToken = decodeURIComponent(`${token}`);

  if (!token)
    return {
      props: {
        error: {
          message: "No access token provided",
        },
      },
    };

  try {
    const response = await Groupevent.post("/event_tokens", { token: decodedToken });

    if (response.status === 200)
      return {
        props: {
          event: response.data as Event,
          token: token
        },
      };

    return {
      props: {
        error: {
          message: response.data.message,
        },
      },
    };
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