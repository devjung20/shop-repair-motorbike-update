import { Col, Form, InputNumber, Row, Select, Table } from "antd";
import { useEffect, useMemo, useState } from "react";
import { apiCaller } from "../../utils/api";
import productAPI from "../../api/requests/product";
import { moneyRenderer } from "../../utils/renderer";
import { intToString, stringToInt } from "../../utils/converter";

const FormOrderProduct = ({
  form,
  onFinish,
  initialValues = { products: [] },
  setTotal = (value) => console.log(value),
}) => {
  const [selectOptions, setSelectOptions] = useState([]);
  const [selected, setSelected] = useState([]);
  const [lines, setLines] = useState([]);
  const totalPrice = useMemo(() => lines.reduce((sum, line) => (sum += line.price * line.quantity), 0), [lines]);

  useEffect(() => {
    setTotal(totalPrice);
  }, [totalPrice, setTotal]);

  useEffect(() => {
    if (initialValues?.products) {
      const productLines = initialValues.products.map((prd) => ({
        ...prd,
        oldQuantity: prd.quantity,
        totalPrice: prd.price * prd.quantity,
      }));
      const productSelected = productLines.map((line) => line.id);
      setLines(productLines);
      setSelected(productSelected);
    }
  }, [initialValues?.products]);

  useEffect(() => {
    apiCaller(
      productAPI.getAll({
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        let dataOpts = data?.data?.content;
        dataOpts = dataOpts?.filter((opt) => opt.storageQuantity > 0);
        dataOpts = dataOpts.map((opt) => ({
          value: opt.id,
          label: opt.name,
          ...opt,
        }));
        setSelectOptions(dataOpts);
      }
    );
  }, []);

  const handleSelectChange = (values) => {
    setSelected(values);

    // If select 0 option
    if (values.length === 0) {
      setLines([]);
      return;
    }

    const lastLineId = values[values.length - 1];

    // If add option
    if (values.length > lines.length) {
      const lineProduct = selectOptions.find((opt) => opt.value === lastLineId);
      const newLine = {
        ...lineProduct,
        price: lineProduct?.priceOut,
        quantity: 1,
        oldQuantity: 0,
        totalPrice: lineProduct?.priceOut,
      };
      setLines([...lines, newLine]);
    }

    // If remove option
    else if (values.length < lines.length) {
      const newLines = lines.filter((line) => values.includes(line.id));
      setLines(newLines);
    }
  };

  const columns = [
    {
      title: "Tên linh kiện",
      dataIndex: "name",
      width: "30%",
    },
    {
      title: "Đơn vị",
      dataIndex: "unit",
      align: "center",
    },
    {
      title: "Đơn giá",
      dataIndex: "price",
      align: "center",
      render: moneyRenderer,
    },
    {
      title: "Có sẵn",
      dataIndex: "storageQuantity",
      align: "center",
      render: (storageQuantity, line) => storageQuantity + line.oldQuantity,
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      align: "center",
      render: (quantity, line) => (
        <InputNumber
          step={1}
          min={1}
          max={line.storageQuantity + line.oldQuantity}
          formatter={intToString}
          parser={stringToInt}
          value={quantity}
          onChange={(value) => {
            const newLines = [...lines];
            const newLine = newLines.find((l) => l.id === line.id);
            newLine.quantity = value;
            newLine.totalPrice = newLine.price * value;
            setLines(newLines);
          }}
          onStep={(value) => {
            const newLines = [...lines];
            const newLine = newLines.find((l) => l.id === line.id);
            newLine.quantity = value;
            newLine.totalPrice = newLine.price * value;
            setLines(newLines);
          }}
        />
      ),
    },
    {
      title: "Thành tiền",
      dataIndex: "totalPrice",
      align: "center",
      render: moneyRenderer,
    },
  ];

  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={() => {
        onFinish({ products: lines });
      }}
    >
      <Row gutter={12}>
        <Col span={24}>
          <Select
            showSearch
            value={selected}
            mode="multiple"
            className="w-100 mb-6"
            placeholder="Chọn linh kiện"
            onChange={handleSelectChange}
            filterOption={(input, option) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase())}
            optionFilterProp="children"
            optionLabelProp="label"
            options={selectOptions}
          />
        </Col>
        <Col span={24} className="mb-6">
          <Table
            size="small"
            columns={columns}
            pagination={false}
            rowKey={(record) => record.id}
            dataSource={lines}
            scroll={{
              x: 576,
            }}
          />
          <div className="mt-6 text-right pr-6">
            <span className="text-semibold">Tổng cộng:</span> {moneyRenderer(totalPrice)}
          </div>
        </Col>
      </Row>
    </Form>
  );
};

export default FormOrderProduct;
