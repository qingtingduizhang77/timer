package com.timer.banjian.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBjResults is a Querydsl query type for BjResults
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBjResults extends EntityPathBase<BjResults> {

    private static final long serialVersionUID = -732283380L;

    public static final QBjResults bjResults = new QBjResults("bjResults");

    public final com.timer.common.entity.QBaseSimpleEntity _super = new com.timer.common.entity.QBaseSimpleEntity(this);

    public final StringPath cdBatch = createString("cdBatch");

    public final StringPath cdOperation = createString("cdOperation");

    public final DateTimePath<java.util.Date> cdTime = createDateTime("cdTime", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath itemCode = createString("itemCode");

    //inherited
    public final DateTimePath<java.util.Date> lastmodi = _super.lastmodi;

    public final StringPath orgCode = createString("orgCode");

    public final StringPath orgName = createString("orgName");

    public final StringPath projectNo = createString("projectNo");

    public final StringPath regionCode = createString("regionCode");

    public final StringPath resultDate = createString("resultDate");

    public final StringPath resultExplain = createString("resultExplain");

    public final StringPath resultType = createString("resultType");

    public final StringPath rowGuid = createString("rowGuid");

    public final StringPath sblsh = createString("sblsh");

    public final StringPath sblshShort = createString("sblshShort");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath taskHandleItem = createString("taskHandleItem");

    public final StringPath userName = createString("userName");

    //inherited
    public final NumberPath<Long> version = _super.version;

    public final StringPath year = createString("year");

    public QBjResults(String variable) {
        super(BjResults.class, forVariable(variable));
    }

    public QBjResults(Path<? extends BjResults> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBjResults(PathMetadata metadata) {
        super(BjResults.class, metadata);
    }

}

