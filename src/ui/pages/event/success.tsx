import React, { useState } from "react";
import { NextPage } from "next";
import Card from "../../components/Card";
import Text from "../../components/Text";
import Flex from "../../components/Flex";
import Button from "../../components/Button";
import {sessionOptions} from "../../lib/session";
import {withIronSessionSsr} from "iron-session/next";
import { useRouter } from 'next/router';
import Confetti from 'react-confetti';
import { useWindowSize } from 'react-use';

const EventSuccess: NextPage = () => {
  const [isRouting, setIsRouting] = useState(false);
  const router = useRouter();
  const { width, height } = useWindowSize();

  const routeToCreateEventPage = async () => {
    setIsRouting(true);
    await router.push('/event/create');
  }

  return (
    <>
      <Confetti width={width} height={height} />
      <Card css={{height: 350}}>
        <Flex css={{ flexDirection: "column", height: '100%', justifyContent: 'space-between' }}>
          <Flex
            css={{ gap: 20, flexDirection: "column", alignItems: "center" }}
          >
            <Flex
              css={{ gap: 10, flexDirection: "column", alignItems: "center" }}
            >
              <Text css={{ fontSize: 60 }}>🎉</Text>
              <Text css={{ fontSize: 32, fontWeight: 500 }}>Hooray!</Text>
            </Flex>

            <Text css={{ textAlign: "center" }}>
              You’re on your way to party town! You’ll be notified of RSVP
              responses by email.
            </Text>
          </Flex>
          <Button onClick={routeToCreateEventPage} loading={isRouting} loadingText='Navigating...'>Create Another Event</Button>
        </Flex>
      </Card>
    </>
  );
};

export const getServerSideProps = withIronSessionSsr(async function ({
  req,
  res,
}) {
  const { user } = req.session;

  if (!user) {
    return {
      redirect: {
        destination: "/",
        permanent: false,
      },
    };
  }

  return {
    props: {
      session: user,
    },
  };
},
sessionOptions);

export default EventSuccess;
