import { InfoCircleOutlined } from "@ant-design/icons";
import { Popover } from "antd";
import { styled } from "styled-components";

const StyledPopover = styled(Popover)`
  margin-left: 0.5rem;
  & svg {
    font-size: ${({ iconSize }) => iconSize}px;
    color: ${({ iconColor }) => iconColor};
  }
`;

const PopoverContent = styled.div`
  max-width: 300px;
  text-align: justify;
`;

const InfoPopover = ({
  title = "Lưu ý",
  icon = <InfoCircleOutlined />,
  iconSize = 16,
  iconColor = "#808080",
  label,
  children,
}) => {
  return (
    <>
      {label}
      <StyledPopover
        title={title}
        trigger="hover"
        iconSize={iconSize}
        iconColor={iconColor}
        content={<PopoverContent>{children}</PopoverContent>}
      >
        {icon}
      </StyledPopover>
    </>
  );
};

export default InfoPopover;
