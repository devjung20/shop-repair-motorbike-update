import { styled } from "styled-components";

const StyledLine = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
`;

const StyledTitle = styled.div`
  font-weight: 600;
  width: 100px;
`;

const StyledText = styled.div`
  width: calc(100% - 100px);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const CardData = ({ data = {}, labels = [] }) => {
  return (
    <>
      {labels.map((label) => {
        let value = data[label.dataIndex];

        if (label.render) {
          value = label.render(value);
        }

        return (
          <StyledLine key={label.dataIndex}>
            <StyledTitle>{label.title}</StyledTitle>
            <StyledText>{value}</StyledText>
          </StyledLine>
        );
      })}
    </>
  );
};

export default CardData;
