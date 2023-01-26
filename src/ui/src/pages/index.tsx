import React from "react";
import { NextPage } from "next";
import Card from "@/components/Card";
import Logo from "@/components/Logo";
import { styled } from "@/stitches.config";
import Box from "@/components/Box";
import GettingStarted from "@/containers/GettingStarted";

const LogoBox = styled(Box, {
  marginBottom: 30,
});

const Home: NextPage = () => {
  return (
    <>
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
