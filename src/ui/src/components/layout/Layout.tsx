import React from "react";
import Container from "@/components/layout/Container";
import FullHeight from "@/components/layout/FullHeight";

interface Props {
  children: React.ReactNode | React.ReactNode[];
}

const Layout: React.FC<Props> = ({ children }) => {
  return (
    <FullHeight>
      <Container>{children}</Container>
    </FullHeight>
  );
};

export default Layout;
