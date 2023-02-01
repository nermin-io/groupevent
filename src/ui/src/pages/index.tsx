import React from "react";
import { NextPage } from "next";
import Card from "@/components/Card";
import Logo from "@/components/Logo";
import { styled } from "@/stitches.config";
import Box from "@/components/Box";
import GettingStarted from "@/containers/GettingStarted";
import DocumentHead from "@/components/DocumentHead";

const LogoBox = styled(Box, {
  marginBottom: 30,
});

const Home: NextPage = () => {
  return (
    <>
      <DocumentHead
        title="Groupevent - Get Started"
        description="Groupevent is an event invite platform that allows event organisers to leverage emails for event management."
      />
      <Card>
        <LogoBox>
          <Logo />
        </LogoBox>
        <GettingStarted />
      </Card>
    </>
  );
};

export default Home;
