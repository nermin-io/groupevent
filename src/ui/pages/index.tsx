import React from 'react';
import { NextPage } from "next";
import Card from "../components/Card";
import Logo from "../components/Logo";
import Text from "../components/Text";
import { styled } from '../stitches.config';
import Box from "../components/Box";
import GettingStarted from "../containers/GettingStarted";

const LogoBox = styled(Box, {
   marginBottom: 30
});

const Home: NextPage = () => {
  return (
      <>
        <Card>
            <LogoBox>
                <Logo />
            </LogoBox>
            <Text css={{lineHeight: 1.375, fontWeight: 450}}>
                Easily invite friends to your event. <br/>
                No signup, no marketing, no bs.
            </Text>
            <GettingStarted />

        </Card>
      </>
  );
}

export default Home;