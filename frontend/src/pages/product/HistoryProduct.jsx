import { Table, Tag } from "antd";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import useTableSearch from "../../hooks/useTableSearch";
import ListPageLayout from "../../layouts/ListPageLayout";
import TableRowExpand from "../../components/TableRowExpand";
import {
  dateTimeRenderer,
  defaultRenderer,
  differentRenderer,
  historyActionRenderer,
  moneyRenderer,
} from "../../utils/renderer";
import queryString from "query-string";
import { apiCaller } from "../../utils/api";
import { Excel } from "antd-table-saveas-excel";
import productAPI from "../../api/requests/product";

const labels = [
  { title: "Thời gian", dataIndex: "createAt", render: dateTimeRenderer },
  { title: "Linh kiện", dataIndex: "productName", render: defaultRenderer },
  { title: "Đơn vị", dataIndex: "unit", render: defaultRenderer },
  { title: "Giá nhập", dataIndex: "priceIn", render: moneyRenderer },
  { title: "Giá xuất", dataIndex: "priceOut", render: moneyRenderer },
  { title: "Ghi chú", dataIndex: "note", render: defaultRenderer },
  { title: "Phân loại", dataIndex: "action", render: historyActionRenderer },
  { title: "Thay đổi", dataIndex: "difference", render: differentRenderer },
  { title: "Còn lại", dataIndex: "quantityLeft", render: defaultRenderer },
];

const HistoryProduct = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const params = queryString.parse(location.search);

  const [pagination, setPagination] = useState({
    size: "default",
    hideOnSinglePage: true,
    showTotal: (total, range) => `Hiển thị ${range[0]} - ${range[1]} trên tổng số ${total}`,
    responsive: true,
    showLessItem: true,
  });

  const columns = [
    {
      title: "Thời gian",
      dataIndex: "createDate",
      render: dateTimeRenderer,
      defaultSortOrder: params.sortBy === "createDate" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
    },
    {
      ...useTableSearch("productName", params.productName),
      defaultFilteredValue: params.productName ? [params.productName] : null,
      title: "Linh kiện",
      dataIndex: "productName",
      align: "center",
      render: defaultRenderer,
    },
    {
      ...useTableSearch("note", params.note),
      defaultFilteredValue: params.note ? [params.note] : null,
      title: "Ghi chú",
      dataIndex: "note",
      align: "center",
      render: defaultRenderer,
    },
    {
      title: "Phân loại",
      dataIndex: "action",
      align: "center",
      render: (action) => (
        <Tag color={action === "IMPORT" ? "#66B8FF" : "error"} key={action} style={{ borderRadius: 50 }}>
          {historyActionRenderer(action)}
        </Tag>
      ),
      filters: [
        { text: "Xuất kho", value: "EXPORT" },
        { text: "Nhập kho", value: "IMPORT" },
      ],
      defaultFilteredValue: params.action ? [params.action] : null,
      filterMultiple: false,
      onFilter: () => true,
    },
    {
      title: "Thay đổi",
      dataIndex: "difference",
      align: "center",
      defaultSortOrder: params.sortBy === "difference" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
      render: differentRenderer,
    },
    {
      title: "Còn lại",
      dataIndex: "quantityLeft",
      align: "center",
      defaultSortOrder: params.sortBy === "quantityLeft" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
      render: defaultRenderer,
    },
  ];

  const [dataSource, setDataSource] = useState([]);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      productAPI.getHistory({
        ...params,
        pageNumber: params.pageNumber || 1,
        pageSize: params.pageSize || 5,
      }),
      (data) => {
        setDataSource(data.data.content);
        setPagination({
          ...pagination,
          current: data.data.number + 1,
          pageSize: data.data.size,
          total: data.data.totalElements,
        });
        setLoading(false);
      }
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [JSON.stringify(params), JSON.stringify(pagination)]);

  const handleTableChange = (pagination, filters, sorter) => {
    const currParams = { ...params };
    // Apply new page number
    currParams.pageNumber = pagination.current;
    currParams.pageSize = pagination.pageSize;
    // Apply new search params
    Object.keys(filters).forEach((key) => {
      if (filters[key]) {
        currParams[key] = filters[key][0];
      } else {
        delete currParams[key];
      }
    });
    // Apply new sort params
    if (sorter.field && sorter.order) {
      currParams.sortBy = sorter.field;
      currParams.sortOrder = sorter.order;
    }

    navigate("/stock-history?" + queryString.stringify(currParams));
  };

  const exportTable = () => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      productAPI.getHistory({
        ...params,
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        new Excel()
          .addSheet("Lịch sử kho")
          .addColumns(labels)
          .addDataSource(data.data.content, { str2Percent: true })
          .saveAs(`Lịch sử kho - ${dateTimeRenderer()}.xlsx`);

        setLoading(false);
      }
    );
  };

  return (
    <ListPageLayout
      title="Lịch sử"
      cardTitle="Lịch sử xuất nhập kho"
      exportText="Xuất danh sách"
      addable={false}
      handleExport={exportTable}
    >
      <Table
        size="small"
        columns={columns}
        rowKey={(record) => record.id}
        dataSource={dataSource}
        pagination={pagination}
        loading={loading}
        expandable={{
          expandedRowRender: (record) => <TableRowExpand record={record} labels={labels} />,
        }}
        scroll={{
          x: 576,
        }}
        onChange={handleTableChange}
      />
    </ListPageLayout>
  );
};

export default HistoryProduct;
