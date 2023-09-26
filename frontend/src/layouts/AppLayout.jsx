import { Layout } from "antd";
import NavDrawer from "./NavDrawer";
import AppHeader from "./AppHeader";
import { Content } from "antd/es/layout/layout";
import { styled } from "styled-components";

const StyledLayout = styled(Layout)`
  height: 100vh;
  animation: slideIn 0.5s ease;
`;

const StyledContent = styled(Content)`
  overflow-y: auto;
  height: calc(100vh - 64px);
  padding: 2rem;
`;

const AppLayout = ({ children }) => {
  return (
    <StyledLayout>
      <NavDrawer />
      <Layout>
        <AppHeader />
        <StyledContent>{children}</StyledContent>
      </Layout>
    </StyledLayout>
  );
};

export default AppLayout;
