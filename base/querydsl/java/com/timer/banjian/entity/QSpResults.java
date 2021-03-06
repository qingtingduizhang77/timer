package com.timer.banjian.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpResults is a Querydsl query type for SpResults
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSpResults extends EntityPathBase<SpResults> {

    private static final long serialVersionUID = 472689239L;

    public static final QSpResults spResults = new QSpResults("spResults");

    public final com.timer.common.entity.QBaseSimpleEntity _super = new com.timer.common.entity.QBaseSimpleEntity(this);

    public final StringPath bizType = createString("bizType");

    public final StringPath certNo = createString("certNo");

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.util.Date> lastmodi = _super.lastmodi;

    public final StringPath remark = createString("remark");

    public final StringPath spStatus = createString("spStatus");

    public final StringPath status = createString("status");

    //inherited
    public final NumberPath<Long> version = _super.version;

    public final StringPath year = createString("year");

    public QSpResults(String variable) {
        super(SpResults.class, forVariable(variable));
    }

    public QSpResults(Path<? extends SpResults> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpResults(PathMetadata metadata) {
        super(SpResults.class, metadata);
    }

}

