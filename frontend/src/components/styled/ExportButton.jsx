import { CloudDownloadOutlined } from "@ant-design/icons";
import { Button } from "antd";
import { styled } from "styled-components";

const ExportButtonWrapper = styled(Button)`
  display: flex;
  align-items: center;
  padding-left: 0;
  padding-right: 0;

  svg {
    font-size: 23px;
  }
`;

const ExportButton = ({ children, onClick = () => {} }) => {
  return (
    <ExportButtonWrapper type="link" onClick={onClick}>
      <CloudDownloadOutlined /> {children}
    </ExportButtonWrapper>
  );
};

export default ExportButton;
