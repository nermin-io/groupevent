import { GetServerSideProps, NextPage } from "next";
import React from "react";
import Card from "@/components/Card";
import Flex from "@/components/Flex";
import Text from "@/components/Text";
import { Event } from "@/clients/groupevent/types";
import { verifyToken } from "@/helpers";
import EventCancelForm from "@/containers/EventCancelForm";
import DocumentHead from "@/components/DocumentHead";

interface PageProps {
  event: Event;
  token: string;
  error?: {
    message: string;
  };
}


interface ErrorProps {
  message: string;
}

const ErrorMessage: React.FC<ErrorProps> = ({ message }) => {
  return (
    <Card>
      <DocumentHead
        title="Groupevent - Cancel Event"
        description="Groupevent is an event invite platform that allows event organisers to leverage emails for event management."
      />
      <Flex css={{ gap: 20, flexDirection: "column" }}>
        <Text css={{ fontSize: 28, fontWeight: 500 }}>Error</Text>
        <Text>{message}</Text>
      </Flex>
    </Card>
  );
};

const CancelEvent: NextPage<PageProps> = ({ event, error, token }) => {
  if (error) return <ErrorMessage message={error.message} />;
  return (
    <>
      <DocumentHead
        title="Groupevent - Cancel Event"
        description="Groupevent is an event invite platform that allows event organisers to leverage emails for event management."
      />
      <Card>
        <EventCancelForm event={event} token={token} />
      </Card>
    </>
  );
};

export default CancelEvent;

export const getServerSideProps: GetServerSideProps = async ({ query }) => {
  const { token } = query;
  return await verifyToken(token);
};