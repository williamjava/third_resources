/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * The Class DefaultCommentGenerator. 默认的注释生成
 * 
 * @author Jeff Butler
 */
public class DefaultCommentGenerator implements CommentGenerator {

	/** The properties. */
	private Properties properties;

	/**
	 * The suppress date. 禁止日期生成
	 */
	private boolean suppressDate;

	/**
	 * The suppress all comments. 禁止生成所有的注释
	 */
	private boolean suppressAllComments;

	/**
	 * The addition of table remark's comments. If suppressAllComments is true,
	 * this option is ignored
	 */
	/**
	 * 添加表格字段的注释说明 当suppressAllComments为true，这个参数的值将会被忽略
	 */
	private boolean addRemarkComments;

	private SimpleDateFormat dateFormat;

	// 添加作者配置
	private String author;

	/**
	 * Instantiates a new default comment generator.
	 */
	public DefaultCommentGenerator() {
		super();
		properties = new Properties();
		suppressDate = false;
		suppressAllComments = false;
		addRemarkComments = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addJavaFileComment(org.mybatis
	 * .generator.api.dom.java.CompilationUnit)
	 */
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		// add no file level comments by default
	}

	/**
	 * Adds a suitable comment to warn users that the element was generated, and
	 * when it was generated. 因不需要mapper.xml中的注释，此处删除 by xia.chengwei
	 * 
	 * @param xmlElement
	 *            the xml element
	 */
	public void addComment(XmlElement xmlElement) {
		/*
		 * if (suppressAllComments) { return; }
		 * 
		 * xmlElement.addElement(new TextElement("<!--"));
		 * 
		 * StringBuilder sb = new StringBuilder(); sb.append("  WARNING - ");
		 * sb.append(MergeConstants.NEW_ELEMENT_TAG); xmlElement.addElement(new
		 * TextElement(sb.toString())); xmlElement .addElement(new TextElement(
		 * "  This element is automatically generated by MyBatis Generator, do not modify."
		 * ));
		 * 
		 * String s = getDateString(); if (s != null) { sb.setLength(0);
		 * sb.append("  This element was generated on "); sb.append(s);
		 * sb.append('.'); xmlElement.addElement(new
		 * TextElement(sb.toString())); }
		 * 
		 * xmlElement.addElement(new TextElement("-->"));
		 */ }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addRootComment(org.mybatis.
	 * generator.api.dom.xml.XmlElement)
	 */
	public void addRootComment(XmlElement rootElement) {
		// add no document level comments by default
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addConfigurationProperties(
	 * java.util.Properties)
	 */
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);

		suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

		suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));

		addRemarkComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));

		String dateFormatString = properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_DATE_FORMAT);
		if (StringUtility.stringHasValue(dateFormatString)) {
			dateFormat = new SimpleDateFormat(dateFormatString);
		}

		// 添加作者配置
		String authorString = properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_AUTHOR);
		if (StringUtility.stringHasValue(authorString)) {
			author = authorString;
		}
	}

	/**
	 * This method adds the custom javadoc tag for. You may do nothing if you do
	 * not wish to include the Javadoc tag - however, if you do not include the
	 * Javadoc tag then the Java merge capability of the eclipse plugin will
	 * break.
	 *
	 * @param javaElement
	 *            the java element
	 * @param markAsDoNotDelete
	 *            the mark as do not delete
	 */
	protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
		javaElement.addJavaDocLine(" *");
		StringBuilder sb = new StringBuilder();
		sb.append(" * ");
		sb.append(MergeConstants.NEW_ELEMENT_TAG);
		if (markAsDoNotDelete) {
			sb.append(" do_not_delete_during_merge");
		}
		String s = getDateString();
		if (s != null) {
			sb.append(' ');
			sb.append(s);
		}
		javaElement.addJavaDocLine(sb.toString());
	}

	/**
	 * This method returns a formated date string to include in the Javadoc tag
	 * and XML comments. You may return null if you do not want the date in
	 * these documentation elements.
	 * 
	 * @return a string representing the current timestamp, or null
	 */
	protected String getDateString() {
		if (suppressDate) {
			return null;
		} else if (dateFormat != null) {
			return dateFormat.format(new Date());
		} else {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addClassComment(org.mybatis.
	 * generator.api.dom.java.InnerClass,
	 * org.mybatis.generator.api.IntrospectedTable) 添加内部类的注释
	 */
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		innerClass.addJavaDocLine("/**");
		innerClass.addJavaDocLine(" * This class was generated by MyBatis Generator.");

		sb.append(" * This class corresponds to the database table ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerClass.addJavaDocLine(sb.toString());

		addJavadocTag(innerClass, false);

		innerClass.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addTopLevelClassComment(org.
	 * mybatis.generator.api.dom.java.TopLevelClass,
	 * org.mybatis.generator.api.IntrospectedTable) 添加实体类的注释
	 */
	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (suppressAllComments || !addRemarkComments) {
			return;
		}
		// 通过这种方式不能直接获取表备注
		// String remarks = introspectedTable.getRemarks();
		// 重新写方法从数据库中获取表备注的信息
		String remarks = introspectedTable.getFullyQualifiedTable().getRemark();
		// 获取实体类名称
		String entityName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
		StringBuilder sb = new StringBuilder();

		// 添加导入类的信息
		topLevelClass.addJavaDocLine("import org.springframework.format.annotation.DateTimeFormat;");
		topLevelClass.addJavaDocLine("import com.fasterxml.jackson.annotation.JsonFormat;");
		topLevelClass.addJavaDocLine("import io.swagger.annotations.ApiModel;");
		topLevelClass.addJavaDocLine("import io.swagger.annotations.ApiModelProperty;");

		// 添加类注释
		topLevelClass.addJavaDocLine("/**");
		sb.append(" * " + remarks);
		sb.append("\n");
		sb.append(" * 实体类对应的数据表为：  ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		topLevelClass.addJavaDocLine(sb.toString());
		topLevelClass.addJavaDocLine(" * @author " + author);

		// 添加时间
		topLevelClass.addJavaDocLine(" * @date " + getDateString());
		topLevelClass.addJavaDocLine(" */");
		topLevelClass.addJavaDocLine("@ApiModel(value =\"" + entityName + "\")");

		/*
		 * topLevelClass.addJavaDocLine("/**"); if (addRemarkComments &&
		 * StringUtility.stringHasValue(remarks)) {
		 * topLevelClass.addJavaDocLine(" * Database Table Remarks:"); String[]
		 * remarkLines = remarks.split(System.getProperty("line.separator"));
		 * for (String remarkLine : remarkLines) {
		 * topLevelClass.addJavaDocLine(" *   " + remarkLine); } }
		 * topLevelClass.addJavaDocLine(" *");
		 * 
		 * topLevelClass
		 * .addJavaDocLine(" * This class was generated by MyBatis Generator.");
		 * 
		 * sb.append(" * This class corresponds to the database table ");
		 * sb.append(introspectedTable.getFullyQualifiedTable());
		 * topLevelClass.addJavaDocLine(sb.toString());
		 * 
		 * addJavadocTag(topLevelClass, true);
		 */

		// topLevelClass.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addEnumComment(org.mybatis.
	 * generator.api.dom.java.InnerEnum,
	 * org.mybatis.generator.api.IntrospectedTable)
	 */
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		innerEnum.addJavaDocLine("/**");
		innerEnum.addJavaDocLine(" * This enum was generated by MyBatis Generator.");

		sb.append(" * This enum corresponds to the database table ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerEnum.addJavaDocLine(sb.toString());

		addJavadocTag(innerEnum, false);

		innerEnum.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addFieldComment(org.mybatis.
	 * generator.api.dom.java.Field,
	 * org.mybatis.generator.api.IntrospectedTable,
	 * org.mybatis.generator.api.IntrospectedColumn) 添加字段注释
	 */
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}

		// 字段备注信息
		String remarks = introspectedColumn.getRemarks();
		field.addJavaDocLine("@ApiModelProperty(value = \"" + remarks + "\")");

		// 当字段数据类型为date时添加日期注释
		// StringBuffer sb = new StringBuffer();
		// if(introspectedColumn.getJdbcType() == 93) {
		// sb.append("@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
		// }
		//
		// if(sb.length() > 0) {
		// field.addJavaDocLine(sb.toString());
		// }

		/*
		 * field.addJavaDocLine("/**"); if (addRemarkComments &&
		 * StringUtility.stringHasValue(remarks)) {
		 * field.addJavaDocLine(" * Database Column Remarks:"); String[]
		 * remarkLines = remarks.split(System.getProperty("line.separator"));
		 * for (String remarkLine : remarkLines) { field.addJavaDocLine(" *   "
		 * + remarkLine); } }
		 * 
		 * field.addJavaDocLine(" *"); field.
		 * addJavaDocLine(" * This field was generated by MyBatis Generator.");
		 * 
		 * StringBuilder sb = new StringBuilder();
		 * sb.append(" * This field corresponds to the database column ");
		 * sb.append(introspectedTable.getFullyQualifiedTable());
		 * sb.append('.'); sb.append(introspectedColumn.getActualColumnName());
		 * field.addJavaDocLine(sb.toString()); addJavadocTag(field, false);
		 */

		// field.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addFieldComment(org.mybatis.
	 * generator.api.dom.java.Field,
	 * org.mybatis.generator.api.IntrospectedTable)
	 */
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		field.addJavaDocLine("/**");
		field.addJavaDocLine(" * This field was generated by MyBatis Generator.");

		sb.append(" * This field corresponds to the database table ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		field.addJavaDocLine(sb.toString());

		addJavadocTag(field, false);

		field.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addGeneralMethodComment(org.
	 * mybatis.generator.api.dom.java.Method,
	 * org.mybatis.generator.api.IntrospectedTable) toString()上的注释，在此全部注释掉
	 */
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		/*
		 * if (suppressAllComments) { return; }
		 * 
		 * StringBuilder sb = new StringBuilder();
		 * 
		 * method.addJavaDocLine("/**"); method
		 * .addJavaDocLine(" * This method was generated by MyBatis Generator."
		 * );
		 * 
		 * sb.append(" * This method corresponds to the database table ");
		 * sb.append(introspectedTable.getFullyQualifiedTable());
		 * method.addJavaDocLine(sb.toString());
		 * 
		 * addJavadocTag(method, false);
		 */

		// method.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addGetterComment(org.mybatis.
	 * generator.api.dom.java.Method,
	 * org.mybatis.generator.api.IntrospectedTable,
	 * org.mybatis.generator.api.IntrospectedColumn) 给Get方法添加注释
	 */
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		if (introspectedColumn.getJdbcType() == 93) {
			sb.append("@JsonFormat(locale = \"zh\", timezone = \"GMT+8\", pattern = \"yyyy-MM-dd HH:mm:ss\")");
		}

		if (sb.length() > 0) {
			method.addJavaDocLine(sb.toString());
		}

		/*
		 * method.addJavaDocLine("/**"); method.
		 * addJavaDocLine(" * This method was generated by MyBatis Generator.");
		 * sb.append(" * This method returns the value of the database column "
		 * ); sb.append(introspectedTable.getFullyQualifiedTable());
		 * sb.append('.'); sb.append(introspectedColumn.getActualColumnName());
		 * method.addJavaDocLine(sb.toString()); method.addJavaDocLine(" *");
		 * sb.setLength(0); sb.append(" * @return the value of ");
		 * sb.append(introspectedTable.getFullyQualifiedTable());
		 * sb.append('.'); sb.append(introspectedColumn.getActualColumnName());
		 * method.addJavaDocLine(sb.toString()); addJavadocTag(method, false);
		 */

		// method.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addSetterComment(org.mybatis.
	 * generator.api.dom.java.Method,
	 * org.mybatis.generator.api.IntrospectedTable,
	 * org.mybatis.generator.api.IntrospectedColumn) 给Set方法添加注释
	 */
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		/*
		 * if (suppressAllComments) { return; }
		 * 
		 * StringBuilder sb = new StringBuilder();
		 * 
		 * method.addJavaDocLine("/**"); method
		 * .addJavaDocLine(" * This method was generated by MyBatis Generator."
		 * );
		 * 
		 * sb.append(" * This method sets the value of the database column ");
		 * sb.append(introspectedTable.getFullyQualifiedTable());
		 * sb.append('.'); sb.append(introspectedColumn.getActualColumnName());
		 * method.addJavaDocLine(sb.toString());
		 * 
		 * method.addJavaDocLine(" *");
		 * 
		 * Parameter parm = method.getParameters().get(0); sb.setLength(0);
		 * sb.append(" * @param "); sb.append(parm.getName());
		 * sb.append(" the value for ");
		 * sb.append(introspectedTable.getFullyQualifiedTable());
		 * sb.append('.'); sb.append(introspectedColumn.getActualColumnName());
		 * method.addJavaDocLine(sb.toString());
		 * 
		 * addJavadocTag(method, false);
		 */

		// method.addJavaDocLine(" */");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.CommentGenerator#addClassComment(org.mybatis.
	 * generator.api.dom.java.InnerClass,
	 * org.mybatis.generator.api.IntrospectedTable, boolean)
	 */
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		innerClass.addJavaDocLine("/**");
		innerClass.addJavaDocLine(" * This class was generated by MyBatis Generator.");

		sb.append(" * This class corresponds to the database table ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerClass.addJavaDocLine(sb.toString());

		addJavadocTag(innerClass, markAsDoNotDelete);

		innerClass.addJavaDocLine(" */");
	}
}
