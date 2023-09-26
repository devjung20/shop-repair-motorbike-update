import { SearchOutlined } from "@ant-design/icons";
import { Button, Input, Space } from "antd";
import { useRef, useState } from "react";
import Highlighter from "react-highlight-words";

const useTableSearch = (dataIndex, initText) => {
  const [searchText, setSearchText] = useState(initText || "");
  const [searchedColumn, setSearchedColumn] = useState(initText ? dataIndex : "");
  const searchInput = useRef(null);

  const handleSearch = (selectedKeys, confirm, dataIndex) => {
    if (!selectedKeys[0]) {
      setSearchText("");
      setSearchedColumn("");
    } else {
      setSearchText(selectedKeys[0]);
      setSearchedColumn(dataIndex);
    }
    confirm();
  };

  const handleReset = (clearFilters, setSelectedKeys, confirm) => {
    clearFilters();
    setSearchText("");
    setSearchedColumn("");
    setSelectedKeys([]);
    confirm();
  };

  return {
    filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
      <div style={{ padding: 8 }} onKeyDown={(e) => e.stopPropagation()}>
        <Input
          ref={searchInput}
          placeholder={"Nhập thông tin tìm kiếm"}
          value={selectedKeys[0]}
          onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
          onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
          style={{ marginBottom: 8, display: "block" }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{ width: "100px" }}
          >
            Tìm kiếm
          </Button>
          <Button
            onClick={() => clearFilters && handleReset(clearFilters, setSelectedKeys, confirm)}
            size="small"
            style={{ width: "100px" }}
          >
            Đặt lại
          </Button>
        </Space>
      </div>
    ),
    filtered: searchedColumn === dataIndex,
    filterIcon: (filtered) => <SearchOutlined style={{ color: filtered ? "#1677ff" : undefined }} />,
    onFilter: (value, record) => record[dataIndex]?.toString()?.toLowerCase()?.includes(value?.toLowerCase()),
    // onFilter: () => true,
    onFilterDropdownOpenChange: (visible) => visible && setTimeout(() => searchInput.current?.select(), 100),
    render: (text) =>
      searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
          searchWords={[searchText]}
          autoEscape
          textToHighlight={text ? text.toString() : ""}
        />
      ) : (
        text
      ),
  };
};

export default useTableSearch;
