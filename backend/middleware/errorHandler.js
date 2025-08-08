const errorHandler = (err, req, res, next) => {
  console.error('Error:', err);

  // Sequelize 验证错误
  if (err.name === 'SequelizeValidationError') {
    const errors = err.errors.map(error => ({
      field: error.path,
      message: error.message
    }));
    return res.status(400).json({
      success: false,
      message: '数据验证失败',
      errors
    });
  }

  // Sequelize 唯一约束错误
  if (err.name === 'SequelizeUniqueConstraintError') {
    const field = err.errors[0].path;
    return res.status(400).json({
      success: false,
      message: `${field} 已存在，请使用其他值`
    });
  }

  // Sequelize 外键约束错误
  if (err.name === 'SequelizeForeignKeyConstraintError') {
    return res.status(400).json({
      success: false,
      message: '关联数据不存在或已被引用'
    });
  }

  // JWT 错误
  if (err.name === 'JsonWebTokenError') {
    return res.status(401).json({
      success: false,
      message: '无效的访问令牌'
    });
  }

  if (err.name === 'TokenExpiredError') {
    return res.status(401).json({
      success: false,
      message: '访问令牌已过期'
    });
  }

  // 文件上传错误
  if (err.code === 'LIMIT_FILE_SIZE') {
    return res.status(400).json({
      success: false,
      message: '文件大小超出限制'
    });
  }

  if (err.code === 'LIMIT_UNEXPECTED_FILE') {
    return res.status(400).json({
      success: false,
      message: '不支持的文件类型'
    });
  }

  // 自定义错误
  if (err.status) {
    return res.status(err.status).json({
      success: false,
      message: err.message
    });
  }

  // 默认服务器错误
  res.status(500).json({
    success: false,
    message: process.env.NODE_ENV === 'development' ? err.message : '服务器内部错误'
  });
};

module.exports = errorHandler;