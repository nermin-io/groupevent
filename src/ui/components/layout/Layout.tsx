import React from 'react';
import Container from "./Container";
import FullHeight from "./FullHeight";

interface Props {
    children: React.ReactNode | React.ReactNode[];
}

const Layout: React.FC<Props> = ({children}) => {
    return (
        <FullHeight>
            <Container>
                {children}
            </Container>
        </FullHeight>
    );
}

export default Layout;