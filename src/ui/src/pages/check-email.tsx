import React from "react";
import { NextPage } from "next";
import Card from "@/components/Card";
import Logo from "@/components/Logo";
import { styled } from "@/stitches.config";
import Box from "@/components/Box";
import Text from "@/components/Text";
import Flex from "@/components/Flex";
import DocumentHead from "@/components/DocumentHead";

const LogoBox = styled(Box, {
  marginBottom: 30,
});

const CheckEmail: NextPage = () => {
  return (
    <>
      <DocumentHead
        title="Groupevent - Check your email"
        description="Groupevent is an event invite platform that allows event organisers to leverage emails for event management."
      />
      <Card>
        <LogoBox>
          <Logo />
        </LogoBox>
        <Flex css={{ gap: 20, flexDirection: "column" }}>
          <Text css={{ fontSize: 28, fontWeight: 500 }}>
            A magic link has been sent to your inbox!
          </Text>
          <Text>
            Check your email for instructions to create your first groupevent
            invite!
          </Text>
        </Flex>
      </Card>
    </>
  );
};

export default CheckEmail;
