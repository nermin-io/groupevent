import { GetServerSideProps, NextPage } from "next";
import React from "react";
import Card from "../components/Card";
import Flex from "../components/Flex";
import Text from "../components/Text";
import Groupevent from "../clients/groupevent";
import { sessionOptions } from "../lib/session";
import {withIronSessionApiRoute, withIronSessionSsr} from "iron-session/next";

interface PageProps {
  error: {
    message: string;
  };
}

const VerifyToken: NextPage<PageProps> = ({ error }) => {
  return (
    <>
      <Card>
        <Flex css={{ gap: 20, flexDirection: "column" }}>
          <Text css={{ fontSize: 28, fontWeight: 500 }}>Error</Text>
          <Text>{error.message}</Text>
        </Flex>
      </Card>
    </>
  );
};

export default VerifyToken;

export const getServerSideProps: GetServerSideProps = withIronSessionSsr(
  async ({ query, req, res }) => {
    const { organiser, token } = query;

    try {
      const { status, data } = await Groupevent.patch(
        `/organisers/${organiser}/links/${token}`
      );

      if (status !== 200)
        return {
          props: {
            error: {
              message: data.message
            }
          },
        };

      req.session.user = {
        id: data.id,
        first_name: data.first_name,
        last_name: data.last_name,
        email_address: data.email_address,
      };

      await req.session.save();
      return {
        redirect: {
          destination: "/event/create",
          permanent: false,
        },
      };
    } catch (err) {
      return {
        props: {
          error: {
            message: "Something went wrong. Please try again later",
          },
        },
      };
    }
  },
  sessionOptions
);
